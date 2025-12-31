package com.attri.swissarmy.feature.dictionary.domain;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
  @Override
  public DictionaryRepository get() {
    return newInstance();
  }

  public static DictionaryRepository_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static DictionaryRepository newInstance() {
    return new DictionaryRepository();
  }

  private static final class InstanceHolder {
    private static final DictionaryRepository_Factory INSTANCE = new DictionaryRepository_Factory();
  }
}
