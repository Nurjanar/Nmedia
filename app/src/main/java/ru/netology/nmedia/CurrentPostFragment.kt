package ru.netology.nmedia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.NewPostFragment.Companion.textArg
import ru.netology.nmedia.NewPostFragment.Companion.videoArg
import ru.netology.nmedia.databinding.FragmentCurrentPostBinding

class CurrentPostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        postPage: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCurrentPostBinding.inflate(layoutInflater, postPage, false)
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
        val post = viewModel.returnSelectedPost()
        val adapter = PostsAdapter(object : OnInteractionListener {

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
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
                        textArg = post.content
                        videoArg = post.videoLink
                    })

                viewModel.edit(post)

            }

            override fun onPlayVideo(post: Post) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoLink))
                startActivity(intent)
            }
        }
        )
        binding.postPage.adapter = adapter
        adapter.submitList(post)


        return binding.root
    }
}