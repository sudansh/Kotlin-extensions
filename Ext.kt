//Toast
inline fun <T : Fragment> T.toast(@StringRes message: Int) = context?.toast(message)
inline fun <T : Fragment> T.toast(message: CharSequence?) = context?.toast(message)

fun Context.toast(@StringRes message: Int) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.toast(message: CharSequence?) = message?.let {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

//Keyboard
fun View.showKeyboard() {
	val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
	this.requestFocus()
	imm.showSoftInput(this, 0)
}

fun View.hideKeyboard(): Boolean {
	try {
		val inputMethodManager =
			context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
		return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
	} catch (ignored: RuntimeException) {
	}
	return false
}

//Fragment
inline fun <T : Fragment> T.withArgs(argsBuilder: Bundle.() -> Unit): T =
	this.apply { arguments = Bundle().apply(argsBuilder) }

//LiveData
fun <T> LiveData<T>.observeNullable(owner: LifecycleOwner, observer: (T?) -> Unit) =
	observe(owner, Observer<T> { v -> observer.invoke(v) })

fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, observer: (T) -> Unit) {
	this.observe(owner, Observer {
		it?.let {
			observer(it)
		}
	})
}

inline fun <reified T : ViewModel> FragmentActivity.viewModel() =
	ViewModelProviders.of(this)[T::class.java]

inline fun <reified T : ViewModel> FragmentActivity.lazyViewModel() =
	lazy(LazyThreadSafetyMode.NONE) { viewModel<T>() }

inline fun <reified T : ViewModel> Fragment.viewModel() =
	ViewModelProviders.of(this)[T::class.java]

inline fun <reified T : ViewModel> Fragment.lazyViewModel() =
	lazy(LazyThreadSafetyMode.NONE) { viewModel<T>() }

inline fun <reified T : ViewModel> Fragment.lazyActivityViewModel() =
	lazy(LazyThreadSafetyMode.NONE) { activityViewModel<T>() }

inline fun <reified T : ViewModel> Fragment.activityViewModel() =
	ViewModelProviders.of(activity!!)[T::class.java]

fun <X, Y> LiveData<X>.map(transform: (x: X) -> Y): LiveData<Y> {
	return Transformations.map(this) {
		return@map transform(it)
	}
}

fun <X, Y> LiveData<X>.switchMap(transform: (x: X) -> LiveData<Y>): LiveData<Y> {
	return Transformations.switchMap(this) {
		return@switchMap transform(it)
	}
}

fun <X, Y> MediatorLiveData<X>.map(transform: (x: X) -> Y): LiveData<Y> {
	return Transformations.map(this) {
		return@map transform(it)
	}
}

fun <X, Y> MediatorLiveData<X>.switchMap(transform: (x: X) -> LiveData<Y>): LiveData<Y> {
	return Transformations.switchMap(this) {
		return@switchMap transform(it)
	}
}

//Class
fun Any.className(): String = this::class.java.simpleName
