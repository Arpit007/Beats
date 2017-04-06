Hi,

As the problem provided was to play chords as per the string entered by the User.

So here goes the solution:

1. Record of all the Sound Files is made in the file called Sound.json, having details about the chord identifier and its associated .wav file. 
Also, it contains the identifier for silence and its corresponding silence duration.

Benefit: New Sounds, different duration Silences can be added to the App without even touching the App Code.

2. The String entered by the User is split into corresponding tokens. These tokens then act as keys for the Map containing the respective Sounds or Silences.
Matched Keys are then played orderly.

3. Play: To Play the chords
    Stop: To Stop the Playing Chords

And that's pretty much it.

(API 14+)

Arpit Bhatnagar
arpit2011@live.com