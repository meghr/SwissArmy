package com.attri.swissarmy.feature.utilities.cleaner;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class MessageRepository_Factory implements Factory<MessageRepository> {
  private final Provider<Context> contextProvider;

  public MessageRepository_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public MessageRepository get() {
    return newInstance(contextProvider.get());
  }

  public static MessageRepository_Factory create(Provider<Context> contextProvider) {
    return new MessageRepository_Factory(contextProvider);
  }

  public static MessageRepository newInstance(Context context) {
    return new MessageRepository(context);
  }
}
