using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TrashDrop : MonoBehaviour
{
    public TrashPrefab[] trashPrefabs;

    public void Drop()
    {
        int randomIndex = Random.Range(0, trashPrefabs.Length);
        Instantiate(trashPrefabs[randomIndex].prefab, transform.position, Quaternion.identity);
    }
}

[System.Serializable]
public class TrashPrefab
{
    public GameObject prefab;
}
