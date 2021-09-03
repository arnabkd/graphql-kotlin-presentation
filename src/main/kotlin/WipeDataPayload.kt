data class WipeDataPayload(val success: Boolean) {
  companion object {
    val SUCCESS = WipeDataPayload(true)
    val FAILURE = WipeDataPayload(false)
  }
}
