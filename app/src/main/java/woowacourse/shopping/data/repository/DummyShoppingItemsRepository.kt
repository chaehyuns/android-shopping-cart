package woowacourse.shopping.data.repository

import woowacourse.shopping.data.database.AppDatabase
import woowacourse.shopping.data.model.ProductEntity
import woowacourse.shopping.domain.model.ProductWithQuantity
import woowacourse.shopping.domain.repository.ShoppingItemsRepository

class DummyShoppingItemsRepository private constructor(database: AppDatabase) : ShoppingItemsRepository {
    private val productDao = database.productDao()

    override fun insertProducts(products: List<ProductEntity>) {
        threadAction {
            productDao.insertProducts(products)
        }
    }

    override fun productWithQuantityItem(productId: Long): Result<ProductWithQuantity> {
        return runCatching {
            var productItem: ProductWithQuantity? = null
            threadAction {
                productItem = productDao.getProductWithQuantityById(productId)
            }
            productItem ?: throw Exception("Product not found")
        }
    }

    override fun findProductWithQuantityItemsByPage(
        page: Int,
        pageSize: Int,
    ): Result<List<ProductWithQuantity>> {
        return runCatching {
            var productWithQuantities = emptyList<ProductWithQuantity>()
            val offset = page * pageSize

            threadAction {
                productWithQuantities = productDao.getProductWithQuantityByPage(limit = pageSize, offset = offset)
            }

            productWithQuantities
        }
    }

    private fun threadAction(action: () -> Unit) {
        val thread = Thread(action)
        thread.start()
        thread.join()
    }

    companion object {
        @Volatile private var instance: DummyShoppingItemsRepository? = null

        fun getInstance(database: AppDatabase): DummyShoppingItemsRepository =
            instance ?: synchronized(this) {
                instance ?: DummyShoppingItemsRepository(database).also { instance = it }
            }
    }
}
