using UnityEngine;
using TMPro;

public class AttributeUI : MonoBehaviour
{
    private CharacterStats characterStats; 
    public TextMeshProUGUI attackText;
    public TextMeshProUGUI defenseText;

    private void Start()
    {
        GameObject player = GameObject.FindWithTag("Player");
        if (player != null)
        {
            characterStats = player.GetComponent<CharacterStats>();
        }

        if (characterStats != null)
        {
            Update();
        }
    }


    private void Update()
    {
        UpdateAttackUI();
        UpdateDefenseUI();
    }

    public void AddAttack()
    {
        characterStats.AllocateAttribute("Attack");
    }

    public void AddDefense()
    {
        characterStats.AllocateAttribute("Defense");
    }
    
    private void UpdateAttackUI()
    {
        if (attackText != null)
        {
            attackText.text = "Attack: " + characterStats.GetAttack().ToString();
        }
    }

    private void UpdateDefenseUI()
    {
        if (defenseText != null)
        {
            defenseText.text = "Defense: " + characterStats.GetDefense().ToString();
        }
    }
}

