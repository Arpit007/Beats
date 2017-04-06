package com.bhatnagar.arpit.beats;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by Home Laptop on 22-Mar-17.
 */

interface PlayerStopped
{
	void onPlayerStopped();
}

public class Player
{
	private Context context;
	private boolean isPlaying = false;
	private int Index = -1;
	private HashMap<String, MediaPlayer> Tone;
	private HashMap<String, Integer> Silence;
	private String playList[];
	private MediaPlayer currentPlayer;
	private PlayerStopped listener;

	Player(Context context)
	{
		this.context = context;
		loadResources();
	}

	private void playTune()
	{
		String Key = playList[Index];
		if (Tone.containsKey(Key))
		{
			try
			{
				currentPlayer=Tone.get(Key);
				currentPlayer.start();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				playNext();
			}
		}
		else
		if(Silence.containsKey(Key))
		{
			currentPlayer=null;
			new Handler().postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					playNext();
				}
			},Silence.get(Key));
		}
		else playNext();
	}

	private void loadResources()
	{
		Tone = new HashMap<>();
		Silence = new HashMap<>();
		try
		{
			InputStream stream = context.getAssets().open("Sound.json");
			byte Bytes[] = new byte[stream.available()];
			stream.read(Bytes);
			stream.close();
			JSONObject jsonObject = new JSONObject(new String(Bytes));
			JSONArray Sounds = jsonObject.getJSONArray("Sounds");
			JSONArray Silence = jsonObject.getJSONArray("Silence");

			for (int x = 0; x < Sounds.length(); x++)
			{
				JSONObject value = Sounds.getJSONObject(x);
				AssetFileDescriptor afd = context.getAssets().openFd( value.getString("File"));

				MediaPlayer mediaPlayer=new MediaPlayer();
				mediaPlayer.setVolume(1.0f,1.0f);
				mediaPlayer.setLooping(false);
				mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
				{
					@Override
					public void onCompletion(MediaPlayer mediaPlayer)
					{
						playNext();
					}
				});
				mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
				afd.close();
				mediaPlayer.prepare();
				Tone.put(value.getString("Key"),mediaPlayer);
			}
			for (int x = 0; x < Silence.length(); x++)
			{
				JSONObject value = Silence.getJSONObject(x);
				this.Silence.put(value.getString("Key"), value.getInt("Duration"));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void playNext()
	{
		Index++;
		if (Index == playList.length)
		{
			isPlaying = false;
			Index = -1;
			if(listener!=null)
				listener.onPlayerStopped();
		}
		else if(isPlaying)
		{
			playTune();
		}
	}

	void setOnPlayerStopListener(PlayerStopped listener)
	{
		this.listener=listener;
	}

	void loadPlayList(String notes)
	{
		playList = notes.trim().replaceAll("\n"," ").toLowerCase().split(" ");
	}

	void play()
	{
		Index = -1;
		isPlaying = true;
		playNext();
	}

	void stop()
	{
		isPlaying = false;
		if(currentPlayer!=null)
			currentPlayer.stop();
	}

	boolean isPlaying()
	{
		return isPlaying;
	}
}
