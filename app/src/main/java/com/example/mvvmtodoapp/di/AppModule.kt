package com.example.mvvmtodoapp.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mvvmtodoapp.data.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

	@Provides
	@Singleton
	fun providesTodoDatabase(app:Application):TodoDatabase {
		return Room.databaseBuilder(
			context = app,
			klass = TodoDatabase::class.java,
			name = todo_db
		)

	}



}