package me.ajinkyashinde.movieapp.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import me.ajinkyashinde.movieapp.ui.movielist.MovieListAdapter


@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {

    @ActivityScoped
    @Provides
    fun provideMovieListAdapter() = MovieListAdapter(ArrayList())

}