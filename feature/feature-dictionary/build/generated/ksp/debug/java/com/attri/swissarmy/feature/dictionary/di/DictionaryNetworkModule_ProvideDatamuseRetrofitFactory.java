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
public final class DictionaryNetworkModule_ProvideDatamuseRetrofitFactory implements Factory<Retrofit> {
  @Override
  public Retrofit get() {
    return provideDatamuseRetrofit();
  }

  public static DictionaryNetworkModule_ProvideDatamuseRetrofitFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static Retrofit provideDatamuseRetrofit() {
    return Preconditions.checkNotNullFromProvides(DictionaryNetworkModule.INSTANCE.provideDatamuseRetrofit());
  }

  private static final class InstanceHolder {
    private static final DictionaryNetworkModule_ProvideDatamuseRetrofitFactory INSTANCE = new DictionaryNetworkModule_ProvideDatamuseRetrofitFactory();
  }
}
