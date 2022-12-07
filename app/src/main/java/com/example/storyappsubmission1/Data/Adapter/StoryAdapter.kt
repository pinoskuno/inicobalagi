package com.example.storyappsubmission1.Data.Adapter
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyappsubmission1.Activity.Main.DetailActivity
import com.example.storyappsubmission1.R
import com.example.storyappsubmission1.Data.Response.ListStoryR

class StoryAdapter (private val Story: ArrayList<ListStoryR>):
    RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(Story[position])

    }

    override fun getItemCount(): Int = Story.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var StoryPhoto: ImageView = itemView.findViewById(R.id.tv_item_photo)
        private var tvStoryName: TextView = itemView.findViewById(R.id.tv_item_name)
        private var tvStoryDate: TextView = itemView.findViewById(R.id.tv_date)

        fun bind(item: ListStoryR) {
            tvStoryName.text =  item.name
            tvStoryDate.text = item.post.take(10)
            Glide.with(itemView.context).load(item.photoUrl).into(StoryPhoto)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(STORIES, item)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(StoryPhoto, itemView.context.getString(R.string.image)),
                        Pair(tvStoryDate, itemView.context.getString(R.string.name)),
                        Pair(tvStoryDate, itemView.context.getString(R.string.Date))
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        const val STORIES = "stories"
    }
}