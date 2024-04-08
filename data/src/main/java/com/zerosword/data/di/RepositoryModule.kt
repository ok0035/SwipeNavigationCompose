package com.zerosword.data.di

import com.zerosword.data.repositoryimpl.MainRepositoryImpl
import com.zerosword.data.services.MainService
import com.zerosword.domain.reporitory.MainRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun provideMainRepository(mainRepositoryImpl: MainRepositoryImpl): MainRepository

}