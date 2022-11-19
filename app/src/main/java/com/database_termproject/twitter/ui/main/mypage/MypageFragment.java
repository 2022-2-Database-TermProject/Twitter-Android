package com.database_termproject.twitter.ui.main.mypage;

import static com.database_termproject.twitter.utils.GlobalApplication.PASSWORD;
import static com.database_termproject.twitter.utils.GlobalApplication.URL;
import static com.database_termproject.twitter.utils.GlobalApplication.USER;
import static com.database_termproject.twitter.utils.SharedPreferenceManagerKt.getUserId;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.database_termproject.twitter.R;
import com.database_termproject.twitter.data.Post;
import com.database_termproject.twitter.data.User;
import com.database_termproject.twitter.ui.adapter.MypageVPAdapter;
import com.database_termproject.twitter.ui.edit.EditActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MypageFragment extends Fragment {
    View view;
    MypageVPAdapter mypageVPAdapter;

    String nickname;
    String region_id;
    String image;
    int following_num = 0;
    int follower_num = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mypage, container, false);

        initVP();
        view.findViewById(R.id.change_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), EditActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        new GetUserAsyncTask().execute();
    }

    private void initVP(){
        mypageVPAdapter = new MypageVPAdapter(this);

        ViewPager2 viewPager2 = view.findViewById(R.id.mypage_vp);
        viewPager2.setAdapter(mypageVPAdapter);


        ArrayList<String> tabs = new ArrayList<>();
        tabs.add("트윗");
        tabs.add("리트윗");
        tabs.add("좋아요");

        TabLayout tabLayout = view.findViewById(R.id.mypage_tablayout);
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
               tab.setText(tabs.get(position));
            }
        }).attach();
    }

    // 유저 정보 조회 JDBC
    @SuppressLint("StaticFieldLeak")
    public class GetUserAsyncTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "select * from user where id = \"" + getUserId() + "\"";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                if(rs.next()){
                    nickname = rs.getString("nickname");
                    region_id = rs.getString("region_id");
                    image = rs.getString("image");

                    // 팔로잉, 팔로워 수 뽑기
                    String sql2 = "select count(follower_id) as follower_num from follower where user_id = \"" + getUserId() + "\"";
                    rs = stmt.executeQuery(sql2);

                    if(rs.next()){
                        follower_num = rs.getInt("follower_num");
                    }

                    String sql3 = "select count(following_id) as following_num from following where user_id = \"" + getUserId() + "\"";
                    rs = stmt.executeQuery(sql3);

                    if(rs.next()){
                        following_num = rs.getInt("following_num");
                    }


                    return true;
                }
            } catch (Exception e) {
                Log.e("GetUserAsyncTask", "Error reading school information", e);
            }


            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                TextView useridTv = view.findViewById(R.id.mypage_user_id);
                TextView nicknameTv = view.findViewById(R.id.mypage_user_nickname);
                TextView regionTv = view.findViewById(R.id.mypage_region_tv);
                TextView followingTv = view.findViewById(R.id.mypage_following_tv);
                ImageView profileIv = view.findViewById(R.id.mypage_user_profile);

                useridTv.setText("@" + getUserId());
                nicknameTv.setText(nickname);
                regionTv.setText(region_id);

                String following = following_num + " 팔로잉 " + follower_num + " 팔로워";
                followingTv.setText(following);

                if(image!=null){
                    Glide.with(view)
                            .load(image)
                            .apply(new RequestOptions().circleCrop())
                            .into(profileIv);
                }
            }

            this.cancel(true);
        }
    }
}
