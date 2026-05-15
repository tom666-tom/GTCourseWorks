using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;

public class UIManager : MonoBehaviour
{
    public TextMeshProUGUI levelText;          
    public TextMeshProUGUI experienceText;     
    private LevelSystem levelSystem;           

    private void Start()
    {
        GameObject player = GameObject.FindWithTag("Player");
        if (player != null)
        {
            levelSystem = player.GetComponent<LevelSystem>();
        }

        if (levelSystem != null)
        {
            UpdateLevelUI();       
            UpdateExperienceUI();  
        }
    }

    private void Update()
    {
        UpdateLevelUI();
        UpdateExperienceUI();
    }

    private void UpdateLevelUI()
    {
        if (levelSystem != null)
        {
            levelText.text = "Level: " + levelSystem.GetLevel().ToString();
        }
    }

    private void UpdateExperienceUI()
    {
        if (levelSystem != null)
        {
            int experience = levelSystem.GetExperience();
            int experienceToNextLevel = levelSystem.GetExperienceToNextLevel();
            experienceText.text = "Exp: " + experience + " / " + experienceToNextLevel;
        }
    }
}
