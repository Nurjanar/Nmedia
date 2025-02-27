package ru.netology.nmedia.activity


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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

        arguments?.textArg?.let { binding.edit.setText(it) }
        arguments?.videoArg?.let(binding.videoUrl::setText)
        binding.edit.requestFocus()

        binding.ok.setOnClickListener {
            val text = binding.edit.text.toString()
            val videoUrl = binding.videoUrl.text.toString()
            if (text.isNotBlank()) {
                viewModel.changeContent(text, videoUrl)
                viewModel.save()
                AndroidUtils.hideKeyBoard(requireView())
            }
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
        var Bundle.textArg by StringArg
        var Bundle.videoArg by StringArg
    }

}


