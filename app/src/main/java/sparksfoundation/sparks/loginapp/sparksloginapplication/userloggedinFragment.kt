package sparksfoundation.sparks.loginapp.sparksloginapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.facebook.*
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import sparksfoundation.sparks.loginapp.sparksloginapplication.databinding.FragmentUserloggedinBinding


class userloggedinFragment : Fragment() {
    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val args by navArgs<userloggedinFragmentArgs>()
    private lateinit var binding: FragmentUserloggedinBinding

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
        binding =
            DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.fragment_userloggedin,
                null,
                false)
        val user_name= args.user.username
        val user_email = args.user.email
        val user_birthday = args.user.user_birthday
        val user_gender= args.user.user_gender
        val user_profilepic=args.user.profilepic
        binding.userName.text= user_name
        if (user_email!=""){
            binding.textEmail.text = user_email

        }else{
            binding.textEmail.visibility=View.INVISIBLE
            binding.imageEmail.visibility=View.INVISIBLE
            binding.linearemail.visibility=View.INVISIBLE
        }
        if (user_birthday!=""){

            binding.textBirthday.text = user_birthday

        }else{
            binding.textBirthday.visibility= View.INVISIBLE
            binding.imageBirthday.visibility=View.INVISIBLE
            binding.linearbirthday.visibility= View.INVISIBLE

        }
        if (user_gender!=""){

            binding.textGender.text = user_gender

        }else{
            binding.textGender.visibility= View.INVISIBLE
            binding.imagegender.visibility=View.INVISIBLE
            binding.lineargender.visibility= View.INVISIBLE

        }
        if (user_profilepic!=""){
            Log.d("profilepicture", "${user_profilepic.toString()}")
            Glide.with(requireContext()).load(user_profilepic).
            placeholder(R.drawable.user).error(R.drawable.user)
                .into(binding.imageProfile)
        }else{
           binding.imageProfile.setImageResource(R.drawable.user)
        }

        binding.userLogoutButton.setOnClickListener {
            disconnectFromFacebook()
            disconnectfromgoogle()
            val action =
                userloggedinFragmentDirections.actionUserloggedinFragmentToSplashScreenFragment()
            this.findNavController().navigate(action)
        }
        return binding.root
    }

    private fun disconnectfromgoogle() {
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext()!!, gso)
        mGoogleSignInClient.signOut()


    }

    fun disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return
        }
        LoginManager.getInstance().logOut()
        AccessToken.setCurrentAccessToken(null)
        Profile.setCurrentProfile(null)
        AccessToken.expireCurrentAccessToken()

        context?.getSharedPreferences("name", Context.MODE_PRIVATE)?.edit()?.clear()?.apply();
        context?.getSharedPreferences("birthday", Context.MODE_PRIVATE)?.edit()?.clear()?.apply();
        context?.getSharedPreferences("gender", Context.MODE_PRIVATE)?.edit()?.clear()?.apply();
        context?.getSharedPreferences("email", Context.MODE_PRIVATE)?.edit()?.clear()?.apply();
        context?.getSharedPreferences("profile_pic", Context.MODE_PRIVATE)?.edit()?.clear()?.apply();


    }
}