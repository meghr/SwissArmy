package com.attri.swissarmy.feature.dictionary.di;

import com.attri.swissarmy.feature.dictionary.data.FreeDictionaryApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("javax.inject.Named")
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
public final class DictionaryNetworkModule_ProvideFreeDictionaryApiServiceFactory implements Factory<FreeDictionaryApiService> {
  private final Provider<Retrofit> retrofitProvider;

  public DictionaryNetworkModule_ProvideFreeDictionaryApiServiceFactory(
      Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public FreeDictionaryApiService get() {
    return provideFreeDictionaryApiService(retrofitProvider.get());
  }

  public static DictionaryNetworkModule_ProvideFreeDictionaryApiServiceFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new DictionaryNetworkModule_ProvideFreeDictionaryApiServiceFactory(retrofitProvider);
  }

  public static FreeDictionaryApiService provideFreeDictionaryApiService(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(DictionaryNetworkModule.INSTANCE.provideFreeDictionaryApiService(retrofit));
  }
}
