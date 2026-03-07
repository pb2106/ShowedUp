package com.showedup.app.signal;

import android.content.Context;
import com.showedup.app.crypto.HashUtils;
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
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class MultiModalSignalCollector_Factory implements Factory<MultiModalSignalCollector> {
  private final Provider<Context> contextProvider;

  private final Provider<HashUtils> hashUtilsProvider;

  public MultiModalSignalCollector_Factory(Provider<Context> contextProvider,
      Provider<HashUtils> hashUtilsProvider) {
    this.contextProvider = contextProvider;
    this.hashUtilsProvider = hashUtilsProvider;
  }

  @Override
  public MultiModalSignalCollector get() {
    return newInstance(contextProvider.get(), hashUtilsProvider.get());
  }

  public static MultiModalSignalCollector_Factory create(Provider<Context> contextProvider,
      Provider<HashUtils> hashUtilsProvider) {
    return new MultiModalSignalCollector_Factory(contextProvider, hashUtilsProvider);
  }

  public static MultiModalSignalCollector newInstance(Context context, HashUtils hashUtils) {
    return new MultiModalSignalCollector(context, hashUtils);
  }
}
