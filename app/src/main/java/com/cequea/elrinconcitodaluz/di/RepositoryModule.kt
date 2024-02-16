package com.cequea.elrinconcitodaluz.di

import com.cequea.elrinconcitodaluz.data.repository.DebtRepositoryImpl
import com.cequea.elrinconcitodaluz.data.repository.ProductRepositoryImpl
import com.cequea.elrinconcitodaluz.data.repository.SaleRepositoryImpl
import com.cequea.elrinconcitodaluz.domain.repository.DebtRepository
import com.cequea.elrinconcitodaluz.domain.repository.ProductRepository
import com.cequea.elrinconcitodaluz.domain.repository.SaleRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideProductRepository(
        database: FirebaseFirestore
    ): ProductRepository {
        return ProductRepositoryImpl(database)
    }

    @Provides
    @Singleton
    fun provideDebtRepository(
        database: FirebaseFirestore
    ): DebtRepository {
        return DebtRepositoryImpl(database)
    }

    @Provides
    @Singleton
    fun provideSaleRepository(
        database: FirebaseFirestore
    ): SaleRepository {
        return SaleRepositoryImpl(database)
    }
}