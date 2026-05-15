using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using System;

public class LevelSystem : MonoBehaviour
{
    public int level = 1;              // initial level
    public int experience = 0;         // initial exp
    public int experienceToNextLevel = 100; // 100 exp to next level for the entire game
    public int maxLevel = 5; //max level is 5

    public ParticleSystem LevelUpEffect; // Particle effect for leveling up
    public TextMeshProUGUI LevelUpText;  // Text display for leveling up
    private bool isDisplayingText = false;
    public GameObject attributePanel;
    
    public CharacterStats characterStats; 

    public event Action<int> OnLevelUp; // Add the OnLevelUp event
    
    void Start()
    {
        if (attributePanel != null)
        {
            attributePanel.SetActive(false);
        }
    }
    public void AddExperience(int amount)
    {
        if (level < maxLevel) 
        {
            experience += amount;
            CheckLevelUp();
        }
    }


    private void CheckLevelUp()
    {
        if (experience >= experienceToNextLevel)
        {
            level++;
            experience -= experienceToNextLevel; // carry over exp points
            //experienceToNextLevel += 50; we can modify the necessary exp points to level up if we want to
            Debug.Log("I leveled up to " + level);

             OnLevelUp?.Invoke(level); // Trigger the OnLevelUp event

            if (LevelUpEffect != null)
            {
                LevelUpEffect.Play();
            }

            if (LevelUpText != null && !isDisplayingText)
            {
                StartCoroutine(DisplayLevelUpText());
            }
            if (attributePanel != null)
            {
                attributePanel.SetActive(true);
            }
            if (characterStats != null)
            {
                characterStats.ModifyAttack(5); // attack add 5 by leveling up
                characterStats.ModifyDefense(5); // defense add 5 by leveling up
                characterStats.attributePoints++;
                Debug.Log($"Level Up! Attack: {characterStats.GetAttack()}, Defense: {characterStats.GetDefense()}");
            }
        }
        
        if (level == maxLevel)
        {
            experience = experienceToNextLevel;
            Debug.Log("Max Level Reached.");
        }
    }
    
    public int GetLevel()
    {
        return level;
    }


    public int GetExperience()
    {
        return experience;
    }


    public int GetExperienceToNextLevel()
    {
        return experienceToNextLevel;
    }
    private IEnumerator DisplayLevelUpText()
    {
        isDisplayingText = true;
        LevelUpText.gameObject.SetActive(true);
        LevelUpText.alpha = 0;

        float fadeInDuration = 1f;
        float fadeOutDuration = 1f;
        float displayDuration = 2f;
        float elapsedTime = 0;

        // Fade in
        while (elapsedTime < fadeInDuration)
        {
            elapsedTime += Time.deltaTime;
            LevelUpText.alpha = Mathf.Lerp(0, 1, elapsedTime / fadeInDuration);
            yield return null;
        }

        yield return new WaitForSeconds(displayDuration);

        // Fade out
        elapsedTime = 0;
        while (elapsedTime < fadeOutDuration)
        {
            elapsedTime += Time.deltaTime;
            LevelUpText.alpha = Mathf.Lerp(1, 0, elapsedTime / fadeOutDuration);
            yield return null;
        }

        LevelUpText.gameObject.SetActive(false);
        isDisplayingText = false;
    }
    
    public void CloseAttributePanel()
    {
        if (attributePanel != null)
        {
            attributePanel.SetActive(false);
        }
    }
}
