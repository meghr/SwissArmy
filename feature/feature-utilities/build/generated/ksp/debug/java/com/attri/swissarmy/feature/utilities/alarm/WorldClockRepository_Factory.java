package com.attri.swissarmy.feature.utilities.alarm;

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
public final class WorldClockRepository_Factory implements Factory<WorldClockRepository> {
  @Override
  public WorldClockRepository get() {
    return newInstance();
  }

  public static WorldClockRepository_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static WorldClockRepository newInstance() {
    return new WorldClockRepository();
  }

  private static final class InstanceHolder {
    private static final WorldClockRepository_Factory INSTANCE = new WorldClockRepository_Factory();
  }
}
