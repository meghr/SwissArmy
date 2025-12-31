package com.attri.swissarmy.feature.dictionary;

import com.attri.swissarmy.feature.dictionary.domain.DictionaryRepository;
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
public final class DictionaryViewModel_Factory implements Factory<DictionaryViewModel> {
  private final Provider<DictionaryRepository> repositoryProvider;

  public DictionaryViewModel_Factory(Provider<DictionaryRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public DictionaryViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static DictionaryViewModel_Factory create(
      Provider<DictionaryRepository> repositoryProvider) {
    return new DictionaryViewModel_Factory(repositoryProvider);
  }

  public static DictionaryViewModel newInstance(DictionaryRepository repository) {
    return new DictionaryViewModel(repository);
  }
}
