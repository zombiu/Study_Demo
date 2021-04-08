// Copyright (c) 2016, the Dart project authors.  Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

import 'dart:async';

import 'package:async/async.dart';

import 'package:test_api/src/utils.dart'; // ignore: implementation_imports

import '../util/io.dart';
import 'runner_suite.dart';
import 'configuration.dart';
import 'console.dart';
import 'engine.dart';
import 'load_suite.dart';
import 'reporter.dart';

/// Runs [loadSuite] in debugging mode.
///
/// Runs the suite's tests using [engine]. The [reporter] should already be
/// watching [engine], and the [config] should contain the user configuration
/// for the test runner.
///
/// Returns a [CancelableOperation] that will complete once the suite has
/// finished running. If the operation is canceled, the debugger will clean up
/// any resources it allocated.
CancelableOperation debug(
    Engine engine, Reporter reporter, LoadSuite loadSuite) {
  _Debugger? debugger;
  var canceled = false;
  return CancelableOperation.fromFuture(() async {
    // Make the underlying suite null so that the engine doesn't start running
    // it immediately.
    engine.suiteSink.add(loadSuite.changeSuite((runnerSuite) {
      engine.pause();
      return runnerSuite;
    }));

    var suite = await loadSuite.suite;
    if (canceled || suite == null) return;

    await (debugger = _Debugger(engine, reporter, suite)).run();
  }(), onCancel: () {
    canceled = true;
    // Make sure the load test finishes so the engine can close.
    engine.resume();
    if (debugger != null) debugger!.close();
  });
}

// TODO(nweiz): Test using the console and restarting a test once sdk#25369 is
// fixed and the VM service client is released
/// A debugger for a single test suite.
class _Debugger {
  /// The test runner configuration.
  final _config = Configuration.current;

  /// The engine that will run the suite.
  final Engine _engine;

  /// The reporter that's reporting [_engine]'s progress.
  final Reporter _reporter;

  /// The suite to run.
  final RunnerSuite _suite;

  /// The console through which the user can control the debugger.
  ///
  /// This is only visible when th