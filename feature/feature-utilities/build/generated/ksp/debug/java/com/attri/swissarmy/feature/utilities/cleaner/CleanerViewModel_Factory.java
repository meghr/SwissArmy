package com.attri.swissarmy.feature.utilities.cleaner;

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
public final class CleanerViewModel_Factory implements Factory<CleanerViewModel> {
  private final Provider<MessageRepository> repositoryProvider;

  public CleanerViewModel_Factory(Provider<MessageRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public CleanerViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static CleanerViewModel_Factory create(Provider<MessageRepository> repositoryProvider) {
    return new CleanerViewModel_Factory(repositoryProvider);
  }

  public static CleanerViewModel newInstance(MessageRepository repository) {
    return new CleanerViewModel(repository);
  }
}
