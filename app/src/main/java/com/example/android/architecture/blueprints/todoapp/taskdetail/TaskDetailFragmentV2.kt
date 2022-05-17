
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.mysuperdispatch.android.R
import com.mysuperdispatch.android.databinding.FragmentVerificationExpiredBinding
import kotlinx.coroutines.flow.MutableStateFlow

class VerificationExpiredFragment : DialogFragment() {

  private var _binding: FragmentVerificationExpiredBinding? = null
  private var binding: FragmentVerificationExpiredBinding?  == null
  val actionState by lazy { MutableStateFlow(false) }

  override fun onDestroyView() {
    super.onDestroyView()
    binding = null
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return super.onCreateDialog(savedInstanceState).apply {
      requestWindowFeature(Window.FEATURE_NO_TITLE)
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    _binding = FragmentVerificationExpiredBinding.inflate(inflater, container, false)
    binding = _binding
    return binding!!.root
  }

  override fun onStart() {
    super.onStart()
    dialog?.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    binding?.ivClose?.setOnClickListener { dismiss() }
    binding?.btnBecomeVerifiedCarrier?.setOnClickListener {
      dismiss()
      actionState.value = true
    }

    super.onViewCreated(view, savedInstanceState)
  }

}
