package woowacourse.shopping.data

import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.ShoppingItemsRepository

class ShoppingItemsRepositoryImpl() : ShoppingItemsRepository {
    private val items: List<Product> = DummyShoppingItems.items

    override fun getAllProducts(): List<Product> {
        return items
    }

    override fun findProductItem(id: Long): Product? {
        return items.firstOrNull { it.id == id }
    }

    override fun findProductsByPage(
        page: Int,
        pageSize: Int,
    ): List<Product> {
        val fromIndex = page * pageSize
        val toIndex = kotlin.math.min(fromIndex + pageSize, items.size)
        return if (fromIndex < items.size) {
            items.subList(fromIndex, toIndex)
        } else {
            emptyList()
        }
    }
}
