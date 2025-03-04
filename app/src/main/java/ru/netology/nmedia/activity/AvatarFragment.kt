package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentAvatarBinding
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.view.loadCircleCrop
import ru.netology.nmedia.viewmodel.PostViewModel

class AvatarFragment : Fragment() {
    private fun loadAvatar(name: String, image: ImageView) {
        if (name.isNotBlank()) {
            image.loadCircleCrop("${BuildConfig.BASE_URL}/avatars/${name}")
        } else {
            image.setImageResource(R.drawable.ic_no_avatar_100dp)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAvatarBinding.inflate(layoutInflater, container, false)
        val viewModel: PostViewModel by activityViewModels()

        val postId = arguments?.idArg?.toLong()

        viewModel.data.observe(viewLifecycleOwner) { state ->
            if (state.posts.isNotEmpty()) {
                val post = state.posts.find { it.id == postId }
                if (post != null) {
                    val avatarName = post.authorAvatar?.trim().toString()
                    loadAvatar(avatarName, binding.image)
                    binding.avatarUrl.setText(post.authorAvatar)
                    binding.uploadAvatar.setOnClickListener {
                        val name = binding.avatarUrl.text?.trim().toString()
                        loadAvatar(name, binding.image)
                        viewModel.changeContent(post.content, post.videoLink, name)
                        viewModel.save()
                    }
                }
            }
        }
        binding.done.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

    companion object {
        var Bundle.idArg by StringArg
    }

}