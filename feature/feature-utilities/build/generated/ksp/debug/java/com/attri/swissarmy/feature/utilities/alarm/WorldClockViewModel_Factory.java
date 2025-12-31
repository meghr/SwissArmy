package com.attri.swissarmy.feature.utilities.alarm;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class WorldClockViewModel_Factory implements Factory<WorldClockViewModel> {
  private final Provider<WorldClockRepository> repositoryProvider;

  public WorldClockViewModel_Factory(Provider<WorldClockRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public WorldClockViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static WorldClockViewModel_Factory create(
      Provider<WorldClockRepository> repositoryProvider) {
    return new WorldClockViewModel_Factory(repositoryProvider);
  }

  public static WorldClockViewModel newInstance(WorldClockRepository repository) {
    return new WorldClockViewModel(repository);
  }
}
