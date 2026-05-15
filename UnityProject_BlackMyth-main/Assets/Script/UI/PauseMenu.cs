using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class PauseMenu : MonoBehaviour
{
    public GameObject PauseMenuPanel; 
    private bool isPaused = false;
    public AudioSource BackgroundMusicPlayer;
    public GameObject OptionsPanel;
    public Toggle muteCheckmark;
    private bool isMuted = false;

    void Start()
    {
        BackgroundMusicPlayer.mute = muteCheckmark.isOn;
    }
    void Update()
    {
        if (Input.GetKeyDown(KeyCode.Escape))
        {
            if (isPaused)
            {
                ResumeGame();
            }
            else
            {
                PauseGame();
            }
        }
        MuteBackgroundMusic(muteCheckmark.isOn);
    }

    public void PauseGame()
    {
        PauseMenuPanel.SetActive(true);  
        Time.timeScale = 0f;             
        isPaused = true;
        //BackgroundMusicPlayer.Pause();
    }

    public void ResumeGame()
    {
        PauseMenuPanel.SetActive(false); 
        OptionsPanel.SetActive(false);
        Time.timeScale = 1f;             
        isPaused = false;
        // if (!muteCheckmark.isOn) // keep playing
        // {
        //     BackgroundMusicPlayer.Play();
        // }
    }
    
    public void ExitGame()
    {
        Time.timeScale = 1f; 
        SceneManager.LoadScene("Opening"); 
    }
    
    public void OpenOptions()
    {
        PauseMenuPanel.SetActive(false); 
        OptionsPanel.SetActive(true);  
    }
    
    public void CloseOptions()
    {
        OptionsPanel.SetActive(false);  
        PauseMenuPanel.SetActive(true);   
    }
    
    public void MuteBackgroundMusic(bool isMuted)
    {
        BackgroundMusicPlayer.mute = isMuted;
    }
    
    public void TogglePauseMenu()
    {
        if (isPaused)
        {
            ResumeGame();
        }
        else
        {
            PauseGame();
        }
    }
}

