package com.attri.swissarmy.feature.dictionary.domain;

import com.attri.swissarmy.feature.dictionary.data.DatamuseApiService;
import com.attri.swissarmy.feature.dictionary.data.FreeDictionaryApiService;
import com.attri.swissarmy.feature.dictionary.data.MyMemoryApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class DictionaryRepository_Factory implements Factory<DictionaryRepository> {
  private final Provider<FreeDictionaryApiService> freeDictionaryApiProvider;

  private final Provider<DatamuseApiService> datamuseApiProvider;

  private final Provider<MyMemoryApiService> myMemoryApiProvider;

  public DictionaryRepository_Factory(Provider<FreeDictionaryApiService> freeDictionaryApiProvider,
      Provider<DatamuseApiService> datamuseApiProvider,
      Provider<MyMemoryApiService> myMemoryApiProvider) {
    this.freeDictionaryApiProvider = freeDictionaryApiProvider;
    this.datamuseApiProvider = datamuseApiProvider;
    this.myMemoryApiProvider = myMemoryApiProvider;
  }

  @Override
  public DictionaryRepository get() {
    return newInstance(freeDictionaryApiProvider.get(), datamuseApiProvider.get(), myMemoryApiProvider.get());
  }

  public static DictionaryRepository_Factory create(
      Provider<FreeDictionaryApiService> freeDictionaryApiProvider,
      Provider<DatamuseApiService> datamuseApiProvider,
      Provider<MyMemoryApiService> myMemoryApiProvider) {
    return new DictionaryRepository_Factory(freeDictionaryApiProvider, datamuseApiProvider, myMemoryApiProvider);
  }

  public static DictionaryRepository newInstance(FreeDictionaryApiService freeDictionaryApi,
      DatamuseApiService datamuseApi, MyMemoryApiService myMemoryApi) {
    return new DictionaryRepository(freeDictionaryApi, datamuseApi, myMemoryApi);
  }
}
