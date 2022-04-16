package sparksfoundation.sparks.loginapp.sparksloginapplication

import android.R.attr
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import org.json.JSONObject
import sparksfoundation.sparks.loginapp.model.User
import sparksfoundation.sparks.loginapp.sparksloginapplication.databinding.FragmentLoginScreenBinding


lateinit var callbackManager: CallbackManager
private lateinit var user: User
private lateinit var binding: FragmentLoginScreenBinding
private lateinit var gso:GoogleSignInOptions
private lateinit var mGoogleSignInClient:GoogleSignInClient
private  const val RC_SIGN_IN =123

class LoginScreenFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=
            DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.fragment_login_screen,
                null,
                false)
binding.loginWithgoogle.setOnClickListener {

    val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
    startActivityForResult(signInIntent, RC_SIGN_IN)
}
        callbackManager = CallbackManager.Factory.create()
        binding.loginButton.setReadPermissions(listOf("email",
            "public_profile",
            "user_gender",
            "user_birthday"))

        binding.loginButton.registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                }

                override fun onError(error: FacebookException) {
                }

                override fun onSuccess(result: LoginResult) {

                    val graphRequest =
                        GraphRequest.newMeRequest(result?.accessToken) { `object`, response ->

                            getFacebookData(`object`)
                        }
                    val parameters = Bundle()
                    parameters.putString("fields", "id,email,name,gender,birthday")
                    graphRequest.parameters = parameters
                    graphRequest.executeAsync()

                }
            })

        return binding.root
    }

    private fun getFacebookData(obj: JSONObject?) {
        val profile_pic =
            "https://graph.facebook.com/${obj?.getString("id")}/picture?width=200&height=200"
        val name = obj?.getString("name")
        val birthday = obj?.getString("birthday")
        val gender = obj?.getString("gender")
        val email = obj?.getString("email")
        user = User(profile_pic,name, birthday, gender, email)
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString("name", name)
            putString("birthday", birthday)
            putString("gender", gender)
            putString("email", email)
            putString("profile_pic", email)

            apply()
        }
        if (user!=null){
            val action = LoginScreenFragmentDirections
                .actionLoginScreenFragmentToUserloggedinFragment(user)
            this.findNavController().navigate(action)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode === RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
            if (account!=null) {
                val name = account.displayName
                val photo = account.photoUrl
                val email = account.email
                val birthday = ""
                val gender = ""
                val user : User = User(photo.toString(),name,birthday,gender,email)
                findNavController().navigate(LoginScreenFragmentDirections.actionLoginScreenFragmentToUserloggedinFragment(user))
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
           // Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            //updateUI(null)
        }
    }

}