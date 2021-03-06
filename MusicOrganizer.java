import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Collections;

/**
 * A class to hold details of audio tracks.
 * Individual tracks may be played.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public class MusicOrganizer
{
    // An ArrayList for storing music tracks.
    private ArrayList<Track> tracks;
    // A player for the music tracks.
    private MusicPlayer player;
    // A reader that can read music files and load them as tracks.
    private TrackReader reader;
    // Indica si hay una reproduccion en curso
    private boolean playing;

    /**
     * Create a MusicOrganizer
     */
    public MusicOrganizer(String musicFolder)
    {
        tracks = new ArrayList<Track>();
        player = new MusicPlayer();
        reader = new TrackReader();
        playing = false;
        readLibrary(musicFolder); // You can choose wich folder you want to load.
        System.out.println("Music library loaded. " + getNumberOfTracks() + " tracks.");
        System.out.println();
    }

    /**
     * Add a track file to the collection.
     * @param filename The file name of the track to be added.
     */
    public void addFile(String filename)
    {
        tracks.add(new Track(filename));
    }

    /**
     * Add a track to the collection.
     * @param track The track to be added.
     */
    public void addTrack(Track track)
    {
        tracks.add(track);
    }

    /**
     * Play a track in the collection.
     * @param index The index of the track to be played.
     */
    public void playTrack(int index)
    {
        if (playing)
        {
            System.out.println("Hay una reproduccion en curso: imposible iniciar una nueva. Pare primer la que esta sonando ahora mismo");
        }
        else
        {
            if(indexValid(index)) {
                Track track = tracks.get(index);
                track.incrementPlayCount();
                player.startPlaying(track.getFilename());
                playing = true;
                System.out.println("Now playing: " + track.getArtist() + " - " + track.getTitle());
            }
        }
    }
    
    /**
     * Plays a random track.
     */
    public void playRandom()
    {
        Random r = new Random(); // Creaates a object from the class Random.
        int randomTrack = r.nextInt(tracks.size()); // Uses nextInt method from the Random class to take one number between (0, last element's number in the index) and save it in randomTrack.
        playTrack(randomTrack);       
    }
    
    /**
     * Plays the whole track's list in a random order.
     */
    public void playShuffle()
    {
       Collections.shuffle(tracks); // Change the tracks order.
       for (Track track : tracks) 
       {
           System.out.println(track.getDetails());
           player.playSample(track.getFilename());
           track.incrementPlayCount();
       }
    }
    
    /**
     * Plays the whole track's list in a random order, and delete each track from the list once it's already played.
     */
    public void playShuffle2()
    {
       ArrayList<Track> copy = new ArrayList<>();
       copy = (ArrayList)tracks.clone();
       Collections.shuffle(tracks);
       int index = 0;
       for (Track track : copy)
       {
           System.out.println(track.getDetails());
           player.playSample(track.getFilename());
           track.incrementPlayCount();
           copy.remove(index); 
           index++;
       }        
    }

    /**
     * Return the number of tracks in the collection.
     * @return The number of tracks in the collection.
     */
    public int getNumberOfTracks()
    {
        return tracks.size();
    }

    /**
     * List a track from the collection.
     * @param index The index of the track to be listed.
     */
    public void listTrack(int index)
    {
        System.out.print("Track " + index + ": ");
        Track track = tracks.get(index);
        System.out.println(track.getDetails());
    }

    /**
     * Show a list of all the tracks in the collection.
     */
    public void listAllTracks()
    {
        System.out.println("Track listing: ");

        for(Track track : tracks) {
            System.out.println(track.getDetails());
        }
        System.out.println();
    }

    /**
     * Show a list of all the tracks in the collection.
     */
    public void listAllTrackWithIterator()
    {
        Iterator<Track> it = tracks.iterator();
        while (it.hasNext())
        {
            Track track = it.next();
            System.out.println(track.getDetails());
        }
    }

    /**
     * List all tracks by the given artist.
     * @param artist The artist's name.
     */
    public void listByArtist(String artist)
    {
        for(Track track : tracks) {
            if(track.getArtist().contains(artist)) {
                System.out.println(track.getDetails());
            }
        }
    }

    public void findInTitle(String artist)
    {
        for(Track track: tracks)
        {
            if(track.getArtist().contains(artist))
            {
                System.out.println(track.getDetails());
            }
        }
    }

    /**
     * Remove a track from the collection.
     * @param index The index of the track to be removed.
     */
    public void removeTrack(int index)
    {
        if(indexValid(index)) {
            tracks.remove(index);
        }
    }
    
    /**
     * Removes all the introduced artist's tracks from the collection.
     */
    public void removeByArtist(String artist)
    {
        Iterator<Track> it = tracks.iterator();
        while (it.hasNext())
        {
            Track track = it.next();
            if (track.getArtist().contains(artist)) // Uses the Track's method getArtist to find the track's artists that matches with the given one.
            {
                it.remove();
            }
        }
    }
    
    /**
     * Removes all the tracks that matches with the introcuded title.
     */
    public void removeByTitle(String title)
    {
        Iterator<Track> it = tracks.iterator();
        while (it.hasNext())
        {
            Track track = it.next();
            if (track.getTitle().contains(title))// Uses the Track's method getTitle to find the track's titles that matches with the given one.
            {
                it.remove();
            }
        }
    }

    /**
     * Play the first track in the collection, if there is one.
     */
    public void playFirst()
    {
        if (playing)
        {
            System.out.println("Hay una reproduccion en curso: imposible iniciar una nueva. Pare primer la que esta sonando ahora mismo");
        }
        else
        {
            if(tracks.size() > 0) {
                tracks.get(0).incrementPlayCount();            
                player.startPlaying(tracks.get(0).getFilename());
                playing = true;
            }
        }

    }

    /**
     * Stop the player.
     */
    public void stopPlaying()
    {
        player.stop();
        playing = false;
    }

    /**
     * Determine whether the given index is valid for the collection.
     * Print an error message if it is not.
     * @param index The index to be checked.
     * @return true if the index is valid, false otherwise.
     */
    private boolean indexValid(int index)
    {
        // The return value.
        // Set according to whether the index is valid or not.
        boolean valid;

        if(index < 0) {
            System.out.println("Index cannot be negative: " + index);
            valid = false;
        }
        else if(index >= tracks.size()) {
            System.out.println("Index is too large: " + index);
            valid = false;
        }
        else {
            valid = true;
        }
        return valid;
    }

    private void readLibrary(String folderName)
    {
        ArrayList<Track> tempTracks = reader.readTracks(folderName, ".mp3");

        // Put all thetracks into the organizer.
        for(Track track : tempTracks) {
            addTrack(track);
        }
    }

    public void setYearOfTrack(int index, int year)
    {
        if (index >= 0 && index < tracks.size())
        {
            tracks.get(index).setYear(year);
        }

    }

    public void isPlaying()
    {
        if (playing)
        {
            System.out.println("Hay una reproduccion en curso");
        }

        else
        {
            System.out.println("No se est� reproduciendo nada en este elemento");
        }
    }
}
