package sparksfoundation.sparks.loginapp.sparksloginapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.facebook.AccessToken
import com.facebook.Profile
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.delay
import sparksfoundation.sparks.loginapp.model.User
import sparksfoundation.sparks.loginapp.sparksloginapplication.databinding.FragmentLoginScreenBinding
import sparksfoundation.sparks.loginapp.sparksloginapplication.databinding.FragmentSplashScreenBinding


class SplashScreenFragment : Fragment() {
    private lateinit var binding: FragmentSplashScreenBinding

    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("AcessToken", "onCreate: ${AccessToken.getCurrentAccessToken().toString()} + ${AccessToken.getCurrentAccessToken()?.isExpired.toString()}")
      val  fadeIn = AnimationUtils.loadAnimation(requireContext(),R.anim.fadein);

        if( AccessToken.getCurrentAccessToken() !=null && AccessToken.getCurrentAccessToken()?.isExpired!=true){
            if(activity?.getPreferences(Context.MODE_PRIVATE) != null){
                sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)!!


            }else{
                sharedPreferences= null
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=
            DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.fragment_splash_screen,
                null,
                false)
        val anim = AnimationUtils.loadAnimation(requireContext(),R.anim.fadein)
        binding.imageSplash.visibility=View.VISIBLE
        binding.imageSplash.animation= anim
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
             gotospecifiedFragment()

        }
    }

    private  suspend  fun gotospecifiedFragment() {

delay(3000)
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (account!=null){
    val name = account.displayName
    val photo = account.photoUrl
    val email= account.email
    val birthday = ""
    val gender= ""
    val user : User = User(photo.toString(),name,birthday,gender,email)
findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToUserloggedinFragment(user))
} else if (sharedPreferences!= null ){
    val name = sharedPreferences!!.getString("name","")
    val photo=  sharedPreferences!!.getString("profile_pic","")
    val email= sharedPreferences!!.getString("email","")
    val birthday= sharedPreferences!!.getString("birthday","")
    val gender= sharedPreferences!!.getString("gender","")
    val user : User = User(photo.toString(),name,birthday,gender,email)
    findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToUserloggedinFragment(user))

}else{
    findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginScreenFragment())

}

    }
}