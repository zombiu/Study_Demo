package com.hugo.study_dialog_demo.ui.section1

import java.util.*

object DataServer {

    fun getSectionData(): List<MySection> {
        val list: MutableList<MySection> = ArrayList<MySection>()
        for (i in 0..7) {
            list.add(MySection(true, "Section $i"))
            list.add(
                MySection(
                    false,
                    Video(
                        "123", "321"
                    )
                )
            )
            list.add(
                MySection(
                    false,
                    Video(
                        "123", "321"
                    )
                )
            )
            list.add(
                MySection(
                    false,
                    Video(
                        "123", "321"
                    )
                )
            )
            list.add(
                MySection(
                    false,
                    Video("123", "321")
                )
            )
            list.add(
                MySection(
                    false,
                    Video(
                        "123", "321"
                    )
                )
            )
            list.add(
                MySection(
                    false,
                    Video(
                        "123", "321"
                    )
                )
            )
        }
        return list
    }
}