using UnityEngine;

public class CharacterStats : MonoBehaviour
{
    public int baseAttack = 10;   
    public int baseDefense = 5;  

    public int currentAttack;    
    public int currentDefense;   

    public int attributePoints = 0; 

    void Start()
    {

        currentAttack = baseAttack;
        currentDefense = baseDefense;
    }


    public int GetAttack()
    {
        return currentAttack;
    }

    public int GetDefense()
    {
        return currentDefense;
    }
    
    public void ModifyAttack(int amount)
    {
        currentAttack += amount;
        Debug.Log($"Attack changed to: {currentAttack}");
    }

    public void ModifyDefense(int amount)
    {
        currentDefense += amount;
        Debug.Log($"Defense changed to: {currentDefense}");
    }
    
    public void AllocateAttribute(string attributeType)
    {
        if (attributePoints > 0)
        {
            if (attributeType == "Attack")
            {
                ModifyAttack(1); 
            }
            else if (attributeType == "Defense")
            {
                ModifyDefense(1); 
            }
            attributePoints--; 
            Debug.Log($"Remaining attribute points: {attributePoints}");
        }
        else
        {
            Debug.Log("No attribute points left to allocate.");
        }
    }
}