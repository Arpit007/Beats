package com.bhatnagar.arpit.beats;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
	Player player;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		player=new Player(this.getBaseContext());
		player.setOnPlayerStopListener(new PlayerStopped()
		{
			@Override
			public void onPlayerStopped()
			{
				findViewById(R.id.Stop).setVisibility(View.GONE);
				findViewById(R.id.Pause).setVisibility(View.GONE);
				findViewById(R.id.Play).setVisibility(View.VISIBLE);
			}
		});
	}
	public void Play(View v)
	{
		String Text=((EditText)findViewById(R.id.Notes)).getText().toString();
		if(Text.isEmpty())
		{
			Toast.makeText(this,"Enter Some Notes",Toast.LENGTH_SHORT).show();
		}
		else
		{
			((Button)findViewById(R.id.Pause)).setText(R.string.Pause);
			findViewById(R.id.Stop).setVisibility(View.VISIBLE);
			findViewById(R.id.Pause).setVisibility(View.VISIBLE);
			findViewById(R.id.Play).setVisibility(View.GONE);
			player.loadPlayList(Text);
			player.play();
		}
	}

	public void Pause(View view)
	{
		if(player.isPlaying())
		{
			player.pause();
			((Button)findViewById(R.id.Pause)).setText(R.string.Resume);
		}
		else
		{
			player.play();
			((Button)findViewById(R.id.Pause)).setText(R.string.Pause);
		}
	}

	public void Stop(View view)
	{
		findViewById(R.id.Stop).setVisibility(View.GONE);
		findViewById(R.id.Pause).setVisibility(View.GONE);
		findViewById(R.id.Play).setVisibility(View.VISIBLE);
		player.stop();
	}

	public void Sample(View view)
	{
		String Notes="C1 D1 E1 C1 . . .\n" +
				"C1 D1 E1 C1 . . .\n" +
				"E1 F1 G1 . . .\n" +
				"E1 F1 G1 . . .\n" +
				"G1 F1 E1 C1 . .\n" +
				"G1 F1 E1 C1 . .\n" +
				"C1 D1 C1 . .\n" +
				"C1 D1 C1 . .";
		((EditText)findViewById(R.id.Notes)).setText(Notes);
	}
}
