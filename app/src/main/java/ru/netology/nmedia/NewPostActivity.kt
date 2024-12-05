package ru.netology.nmedia

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
        val viewModel: PostViewModel by viewModels()

        binding.cancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            viewModel.cancel()
            finish()
            // AndroidUtils.hideKeyBoard(it)
        }
        binding.ok.setOnClickListener {
            val text = binding.edit.text.toString()
            if (text.isBlank()) {
                setResult(RESULT_CANCELED)
                viewModel.cancel()
            } else {
                setResult(RESULT_OK, Intent().apply { putExtra("text", text) })
            }
            finish()
        }
    }
}

object NewPostContract : ActivityResultContract<String?, String?>() {
    override fun createIntent(context: Context, input: String?): Intent {
        return Intent(context, NewPostActivity::class.java).apply {
            putExtra("text", input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?) =
        intent?.getStringExtra("text")
}
