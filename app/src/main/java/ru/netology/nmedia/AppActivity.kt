package ru.netology.nmedia

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.NewPostFragment.Companion.textArg
import ru.netology.nmedia.NewPostFragment.Companion.videoArg
import ru.netology.nmedia.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        val binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragment_container)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        intent.let {
            if (it.action == Intent.ACTION_SEND) {
                val text = it.getStringExtra(Intent.EXTRA_TEXT)
                if (text.isNullOrBlank()) {
                    Snackbar.make(
                        binding.root,
                        R.string.error_empty_content,
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction(android.R.string.ok) {
                            finish()
                        }
                        .show()
                }
                findNavController(R.id.fragment_container).navigate(
                    R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = text
                        videoArg = text
                    }
                )
            }

        }
    }
}