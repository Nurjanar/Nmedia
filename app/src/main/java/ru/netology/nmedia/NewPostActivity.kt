package ru.netology.nmedia

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edit.setText(intent.getStringExtra("text"))
        binding.videoUrl.setText(intent.getStringExtra("videoUrl"))

        val viewModel: PostViewModel by viewModels()

        binding.cancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            viewModel.cancel()
            finish()
        }
        binding.ok.setOnClickListener {
            val text = binding.edit.text.toString()
            val videoUrl = binding.videoUrl.text.toString().takeIf {
                it.isNotBlank()
            }
            if (text.isBlank()) {
                setResult(RESULT_CANCELED)
                viewModel.cancel()
                finish()
            } else {
                setResult(RESULT_OK, Intent().apply {
                    putExtra("text", text)
                    putExtra("videoUrl", videoUrl)
                })
                finish()
            }
        }
    }
}

data class NewPostResult(val text: String, val videoUrl: String?)
object NewPostContract : ActivityResultContract<Pair<String?, String?>?, NewPostResult?>() {
    override fun createIntent(context: Context, input: Pair<String?, String?>?): Intent {
        return Intent(context, NewPostActivity::class.java).apply {
            input?.let {
                putExtra("text", it.first)
                putExtra("videoUrl", it.second)
            }
        }
    }


    override fun parseResult(resultCode: Int, intent: Intent?): NewPostResult? {
        var result = NewPostResult("", "")
        if (resultCode == Activity.RESULT_OK && intent != null) {
            val text = intent.getStringExtra("text")
            val videoUrl = intent.getStringExtra("videoUrl")
            if (!text.isNullOrBlank()) {
                result = NewPostResult(text, videoUrl)
            }
        } else {
            return null
        }
        return result
    }

}

