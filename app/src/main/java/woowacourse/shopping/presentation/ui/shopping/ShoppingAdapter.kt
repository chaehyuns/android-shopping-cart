package woowacourse.shopping.presentation.ui.shopping

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.databinding.ItemLoadMoreBinding
import woowacourse.shopping.databinding.ItemProductBinding
import woowacourse.shopping.domain.model.Product

class ShoppingAdapter(
    private val viewModel: ShoppingViewModel,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: MutableList<ShoppingItem> = mutableListOf()

    fun submitList(newItems: List<Product>) {
        val currentSize = items.size
        val insertedItems = newItems.map { ShoppingItem.ProductType(it) }

        if (newItems.isNotEmpty()) {
            items.removeAll { it is ShoppingItem.LoadMoreType }
            items.addAll(insertedItems)
            if (newItems.size >= PAGE_SIZE) {
                items.add(ShoppingItem.LoadMoreType)
            }
            notifyItemRangeInserted(currentSize, insertedItems.size + 1)
        } else {
            notifyItemRangeInserted(currentSize, insertedItems.size)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ShoppingItem.ProductType -> VIEW_TYPE_PRODUCT
            is ShoppingItem.LoadMoreType -> VIEW_TYPE_LOAD_MORE
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_PRODUCT -> {
                val binding =
                    ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ShoppingViewHolder(binding)
            }

            VIEW_TYPE_LOAD_MORE -> {
                val binding =
                    ItemLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadMoreViewHolder(binding, viewModel)
            }

            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (holder) {
            is ShoppingViewHolder -> {
                val product = (items[position] as ShoppingItem.ProductType).product
                holder.bind(product, viewModel)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    companion object {
        const val VIEW_TYPE_PRODUCT = 0
        const val VIEW_TYPE_LOAD_MORE = 1
        private const val PAGE_SIZE = 20
    }
}
