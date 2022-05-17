
import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.mysuperdispatch.android.extension.lazyFast
import com.mysuperdispatch.android.utils.Utils
import java.io.File

class ImageLoader {

  class Builder constructor(context: Context) {
    var requestManager: RequestManager = Glide.with(context)
    private lateinit var requestBuilder: RequestBuilder<Drawable>
    lateinit var imageLoaderCallback: ImageLoadingCallback
    private val glideCallback by lazyFast {
      object : RequestListener<Drawable> {
        override fun onLoadFailed(
          e: GlideException?,
          model: Any?,
          target: Target<Drawable>?,
          isFirstResource: Boolean
        ): Boolean {
          imageLoaderCallback.onFailed(e)
          return false
        }

        override fun onResourceReady(
          resource: Drawable?,
          model: Any?,
          target: Target<Drawable>?,
          dataSource: DataSource?,
          isFirstResource: Boolean
        ): Boolean {
          imageLoaderCallback.onSuccess(resource)
          return false
        }
      }
    }

    private fun locaLoad(resourceId: Int): Builder {
      requestBuilder = requestManager.load(resourceId)
      return this
    }

    private fun locaLoad(file: File): Builder {
      requestBuilder = requestManager.load(file)
      return this
    }

    fun load(file: File): Builder {
      return locaLoad(file)
    }

    fun load(resourceId: Int): Builder {
      return locaLoad(resourceId)
    }

    fun load(url: String): Builder {
      requestBuilder = requestManager.load(url)
      return this
    }

    fun isNotTrimEmpty(text: CharSequence?): Boolean {
      return !Utils.isTrimEmpty(text)
    }

    
    
    // todo
    private fun error(resourceId: Int): Builder {
      requestBuilder = requestBuilder.apply(RequestOptions.errorOf(resourceId))
      return this
    }

    fun cache(isEnabled: Boolean): Builder {
      requestBuilder = if (isEnabled) {
        requestBuilder.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
      } else {
        requestBuilder.diskCacheStrategy(DiskCacheStrategy.NONE)
      }
      requestBuilder = requestBuilder.skipMemoryCache(!isEnabled)
      return this
    }

    fun into(imageView: ImageView) {
      requestBuilder.into(imageView)
    }
  }
}

interface ImageLoadingCallback {
  fun onSuccess(drawable: Drawable?)
  fun onFailed(ex: Throwable?)
}
