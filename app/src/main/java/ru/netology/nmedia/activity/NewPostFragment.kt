package ru.netology.nmedia.activity


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNewPostBinding.inflate(layoutInflater, container, false)
        val viewModel: PostViewModel by activityViewModels()

        val postId = arguments?.idArg?.toLong()

        viewModel.data.observe(viewLifecycleOwner) { state ->
            if (state.posts.isNotEmpty()) {
                val post = state.posts.find { it.id == postId }
                if (post != null) {
                    binding.edit.setText(post.content)
                    binding.videoUrl.setText(post.videoLink)
                } else {
                    return@observe
                }
            }
        }
        binding.edit.requestFocus()

        binding.ok.setOnClickListener {
            val text = binding.edit.text.toString()
            val videoUrl = binding.videoUrl.text?.trim().toString()

            if (text.isNotBlank()) {
                viewModel.changeContent(text, videoUrl, null)
                viewModel.save()
                AndroidUtils.hideKeyBoard(requireView())
            }
        }
        viewModel.singleError.observe(viewLifecycleOwner) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.error_title)
            builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
        binding.cancel.setOnClickListener {
            viewModel.cancel()
            findNavController().navigateUp()
        }

        viewModel.postCreated.observe(viewLifecycleOwner) {
            viewModel.loadPosts()
            findNavController().navigateUp()
        }

        return binding.root
    }

    companion object {
        var Bundle.idArg by StringArg
    }

}


