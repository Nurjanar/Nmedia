package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.idArg
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostViewHolder
import ru.netology.nmedia.databinding.FragmentCurrentPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class CurrentPostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        postPage: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCurrentPostBinding.inflate(layoutInflater, postPage, false)
        val viewModel: PostViewModel by activityViewModels()
        val postId = arguments?.idAr?.toLong() ?: -1
        val holder = PostViewHolder(binding.postPage, object : OnInteractionListener {

            override fun onLike(post: Post) {
                viewModel.likeById(post)
            }

            override fun openAvatar(post: Post) {
                findNavController().navigate(
                    R.id.action_currentPostFragment_to_avatarFragment,
                    Bundle().apply {
                        idArg = post.id.toString()
                    })
                viewModel.edit(post)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post)
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, post.content)
                }
                val shareIntent = Intent.createChooser(
                    intent, getString(R.string.chooser_share_post)
                )
                startActivity(shareIntent)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
                findNavController().navigateUp()
            }

            override fun onEdit(post: Post) {
                findNavController().navigate(
                    R.id.action_currentPostFragment_to_newPostFragment,
                    Bundle().apply {
                        idArg = post.id.toString()
                    })

                viewModel.edit(post)

            }

            override fun onPlayVideo(post: Post) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoLink))
                startActivity(intent)
            }
        }
        )

        viewModel.data.observe(viewLifecycleOwner) { state ->
            if (state.posts.isNotEmpty()) {
                val post = state.posts.find { it.id == postId }
                if (post != null) {
                    holder.bind(post)
                } else {
                    return@observe
                }
            }
        }
        return binding.root
    }

    companion object {
        var Bundle.idAr by StringArg
    }
}