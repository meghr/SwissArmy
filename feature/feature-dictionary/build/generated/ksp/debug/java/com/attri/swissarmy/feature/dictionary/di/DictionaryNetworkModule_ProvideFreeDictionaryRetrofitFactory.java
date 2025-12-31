package com.attri.swissarmy.feature.dictionary.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
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
public final class DictionaryNetworkModule_ProvideFreeDictionaryRetrofitFactory implements Factory<Retrofit> {
  @Override
  public Retrofit get() {
    return provideFreeDictionaryRetrofit();
  }

  public static DictionaryNetworkModule_ProvideFreeDictionaryRetrofitFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static Retrofit provideFreeDictionaryRetrofit() {
    return Preconditions.checkNotNullFromProvides(DictionaryNetworkModule.INSTANCE.provideFreeDictionaryRetrofit());
  }

  private static final class InstanceHolder {
    private static final DictionaryNetworkModule_ProvideFreeDictionaryRetrofitFactory INSTANCE = new DictionaryNetworkModule_ProvideFreeDictionaryRetrofitFactory();
  }
}
