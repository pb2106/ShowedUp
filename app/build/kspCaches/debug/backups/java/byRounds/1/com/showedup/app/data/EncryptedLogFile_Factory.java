package com.showedup.app.data;

import android.content.Context;
import com.showedup.app.crypto.AesGcmCipher;
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
public final class EncryptedLogFile_Factory implements Factory<EncryptedLogFile> {
  private final Provider<Context> contextProvider;

  private final Provider<AesGcmCipher> cipherProvider;

  public EncryptedLogFile_Factory(Provider<Context> contextProvider,
      Provider<AesGcmCipher> cipherProvider) {
    this.contextProvider = contextProvider;
    this.cipherProvider = cipherProvider;
  }

  @Override
  public EncryptedLogFile get() {
    return newInstance(contextProvider.get(), cipherProvider.get());
  }

  public static EncryptedLogFile_Factory create(Provider<Context> contextProvider,
      Provider<AesGcmCipher> cipherProvider) {
    return new EncryptedLogFile_Factory(contextProvider, cipherProvider);
  }

  public static EncryptedLogFile newInstance(Context context, AesGcmCipher cipher) {
    return new EncryptedLogFile(context, cipher);
  }
}
