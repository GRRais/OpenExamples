package ru.rayanis.mvvmcompose.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.rayanis.mvvmcompose.feature_note.data.data_source.NoteDatabase
import ru.rayanis.mvvmcompose.feature_note.data.repository.NoteRepositoryImpl
import ru.rayanis.mvvmcompose.feature_note.domain.repository.NoteRepository
import ru.rayanis.mvvmcompose.feature_note.domain.use_case.DeleteNote
import ru.rayanis.mvvmcompose.feature_note.domain.use_case.GetNotes
import ru.rayanis.mvvmcompose.feature_note.domain.use_case.NoteUseCases
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton

    fun provideNoteDatabase(app: Application): NoteDatabase
    {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository)
        )
    }

}