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
import ru.netology.nmedia.databinding.FragmentFeedBinding

class FeedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

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
            }

            override fun onEdit(post: Post) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_newPostFragment,
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

            override fun onPostClick(post: Post) {
                viewModel.selectPost(post)
                findNavController().navigate(
                    R.id.action_feedFragment_to_currentPostFragment
                )
            }
        }
        )
        binding.container.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val new = adapter.currentList.size < posts.size
            adapter.submitList(posts) {
                if (new) {
                    binding.container.smoothScrollToPosition(0)
                }
            }
            if (posts.isEmpty()) {
                binding.emptyStateView.visibility = View.VISIBLE
                binding.container.visibility = View.GONE
            } else {
                binding.emptyStateView.visibility = View.GONE
                binding.container.visibility = View.VISIBLE
            }
        }
        binding.add.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

//        viewModel.edited.observe(viewLifecycleOwner){
//            if (it.id != 0L) {
//                findNavController().navigate(
//                    R.id.action_feedFragment_to_newPostFragment,
//                    Bundle().apply{
//                        textArg =it.content
//                        videoArg=it.videoLink
//                    }
//                )
//            }
//        }

        binding.container.itemAnimator = null

        return binding.root
    }

}



