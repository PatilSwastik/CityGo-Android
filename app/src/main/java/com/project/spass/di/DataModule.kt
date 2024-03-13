package com.project.spass.di

import com.project.spass.data.demo_db.DemoDB
import com.project.spass.data.repository.PassRepositoryImp
import com.project.spass.domain.repository.PassRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideProductRepository(demoDB: DemoDB): PassRepository {
        return PassRepositoryImp(demoDB)
    }

}