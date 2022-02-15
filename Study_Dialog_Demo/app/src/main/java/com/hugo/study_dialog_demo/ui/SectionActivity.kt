package com.hugo.study_dialog_demo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.hugo.study_dialog_demo.R
import com.hugo.study_dialog_demo.databinding.ActivitySectionBinding
import com.hugo.study_dialog_demo.ui.section1.MySection
import com.hugo.study_dialog_demo.ui.section1.SectionQuickAdapter
import com.hugo.study_dialog_demo.ui.section1.Video
import java.util.*

class SectionActivity : AppCompatActivity() {
    lateinit var binding: ActivitySectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySectionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val adapter = SectionQuickAdapter(
            R.layout.item_section_content,
            R.layout.def_section_head,
            getSectionData()
        )

        binding.rvList.setLayoutManager(GridLayoutManager(this, 4))
        binding.rvList.adapter = adapter
    }

    fun getSectionData(): List<MySection>? {
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