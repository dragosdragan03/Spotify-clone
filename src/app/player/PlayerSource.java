package app.player;

import app.audio.Collections.AudioCollection;
import app.audio.Files.AudioFile;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PlayerSource {
    @Getter
    private Enums.PlayerSourceType type;
    @Getter
    private AudioCollection audioCollection;
    @Getter
    private AudioFile audioFile;
    @Getter
    private int index;
    private int indexShuffled;
    private int remainedDuration;
    private final List<Integer> indices = new ArrayList<>();

    public PlayerSource(Enums.PlayerSourceType type, AudioFile audioFile) {
        this.type = type;
        this.audioFile = audioFile;
        this.remainedDuration = audioFile.getDuration();
    }

    public PlayerSource(Enums.PlayerSourceType type, AudioCollection audioCollection) {
        this.type = type;
        this.audioCollection = audioCollection;
        this.audioFile = audioCollection.getTrackByIndex(0);
        this.index = 0;
        this.indexShuffled = 0;
        this.remainedDuration = audioFile.getDuration();
    }

    public PlayerSource(Enums.PlayerSourceType type, AudioCollection audioCollection, PodcastBookmark bookmark) {
        this.type = type;
        this.audioCollection = audioCollection;
        this.index = bookmark.getId();
        this.remainedDuration = bookmark.getTimestamp();
        this.audioFile = audioCollection.getTrackByIndex(index);
    }

    public int getDuration() {
        return remainedDuration;
    }

    public boolean setNextAudioFile(Enums.RepeatMode repeatMode, boolean shuffle) {
        boolean isPaused = false;

        if (type == Enums.PlayerSourceType.LIBRARY) {
            if (repeatMode != Enums.RepeatMode.NO_REPEAT) {
                remainedDuration = audioFile.getDuration();
            } else {
                remainedDuration = 0;
                isPaused = true;
            }
        } else {
            if (repeatMode == Enums.RepeatMode.REPEAT_ONCE || repeatMode == Enums.RepeatMode.REPEAT_CURRENT_SONG || repeatMode == Enums.RepeatMode.REPEAT_INFINITE) {
                remainedDuration = audioFile.getDuration();
            } else if (repeatMode == Enums.RepeatMode.NO_REPEAT) {
                if (shuffle) {
                    if (indexShuffled == indices.size() - 1) {
                        remainedDuration = 0;
                        isPaused = true;
                    } else {
                        indexShuffled++;

                        index = indices.get(indexShuffled);
                        updateAudioFile();
                        remainedDuration = audioFile.getDuration();
                    }
                } else {
                    if (index == audioCollection.getNumberOfTracks() - 1) {
                        remainedDuration = 0;
                        isPaused = true;
                    } else {
                        index++;
                        updateAudioFile();
                        remainedDuration = audioFile.getDuration();
                    }
                }
            } else if (repeatMode == Enums.RepeatMode.REPEAT_ALL) {
                if (shuffle) {
                    indexShuffled = (indexShuffled + 1) % indices.size();
                    index = indices.get(indexShuffled);
                } else {
                    index = (index + 1) % audioCollection.getNumberOfTracks();
                }
                updateAudioFile();
                remainedDuration = audioFile.getDuration();
            }
        }

        return isPaused;
    }

    public void setPrevAudioFile(boolean shuffle) {
        if (type == Enums.PlayerSourceType.LIBRARY) {
            remainedDuration = audioFile.getDuration();
        } else {
            if (remainedDuration != audioFile.getDuration()) {
                remainedDuration = audioFile.getDuration();
            } else {
                if (shuffle) {
                    if (indexShuffled > 0) {
                        indexShuffled--;
                    }
                    index = indices.get(indexShuffled);
                    updateAudioFile();
                    remainedDuration = audioFile.getDuration();
                } else {
                    if (index > 0) {
                        index--;
                    }
                    updateAudioFile();
                    remainedDuration = audioFile.getDuration();
                }
            }
        }
    }

    public void generateShuffleOrder(Integer seed) {
        indices.clear();
        Random random = new Random(seed);
        for (int i = 0; i < audioCollection.getNumberOfTracks(); i++) {
            indices.add(i);
        }
        Collections.shuffle(indices, random);
    }

    public void updateShuffleIndex() {
        for (int i = 0; i < indices.size(); i++) {
            if (indices.get(i) == index) {
                indexShuffled = i;
                break;
            }
        }
    }

    public void skip(int duration) {
        remainedDuration += duration;
        if (remainedDuration > audioFile.getDuration()) {
            remainedDuration = 0;
            index++;
            updateAudioFile();
        } else if (remainedDuration < 0) {
            remainedDuration = 0;
        }
    }

    private void updateAudioFile() {
        setAudioFile(audioCollection.getTrackByIndex(index));
    }

    public void setAudioFile(AudioFile audioFile) {
        this.audioFile = audioFile;
    }

}
