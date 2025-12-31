package com.attri.swissarmy.feature.dictionary.di;

import com.attri.swissarmy.feature.dictionary.data.DatamuseApiService;
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
public final class DictionaryNetworkModule_ProvideDatamuseApiServiceFactory implements Factory<DatamuseApiService> {
  private final Provider<Retrofit> retrofitProvider;

  public DictionaryNetworkModule_ProvideDatamuseApiServiceFactory(
      Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public DatamuseApiService get() {
    return provideDatamuseApiService(retrofitProvider.get());
  }

  public static DictionaryNetworkModule_ProvideDatamuseApiServiceFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new DictionaryNetworkModule_ProvideDatamuseApiServiceFactory(retrofitProvider);
  }

  public static DatamuseApiService provideDatamuseApiService(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(DictionaryNetworkModule.INSTANCE.provideDatamuseApiService(retrofit));
  }
}
