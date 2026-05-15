using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MonsterSpawner : MonoBehaviour
{
    public GameObject monsterPrefab;  // Prefab for the monster
    public float spawnRadius = 10f;   // Radius around the player to spawn the monster
    private LevelSystem playerLevelSystem;
    private Transform playerTransform;

    private void Start()
    {
        // Find the player and their level system
        GameObject player = GameObject.FindWithTag("Player");
        if (player != null)
        {
            playerLevelSystem = player.GetComponent<LevelSystem>();
            playerTransform = player.transform;

            if (playerLevelSystem == null)
            {
                Debug.LogError("Player is missing LevelSystem component!");
                return;
            }

            // Subscribe to the player's level-up event
            playerLevelSystem.OnLevelUp += SpawnMonsterOnLevelUp;
        }
    }

    private void SpawnMonsterOnLevelUp(int playerLevel)
    {
        if (playerTransform == null) return;

        // Calculate a random spawn position around the player
        Vector3 spawnPosition = playerTransform.position + new Vector3(
            Random.Range(-spawnRadius, spawnRadius),
            0,
            Random.Range(-spawnRadius, spawnRadius)
        );

        // Ensure the monster spawns within the allowed area
        spawnPosition = AdjustSpawnPositionWithinBounds(spawnPosition);

        // Spawn the monster prefab at the calculated position
        GameObject newMonster = Instantiate(monsterPrefab, spawnPosition, Quaternion.identity);

        // Scale the monster's difficulty based on the player's level
        Monster monsterScript = newMonster.GetComponent<Monster>();
        if (monsterScript != null)
        {
            monsterScript.player = playerTransform; // Set the player as the target
            monsterScript.ScaleDifficulty(playerLevel); // Scale the monster stats
            Debug.Log($"Spawned a new monster scaled to player level {playerLevel}.");
        }
        else
        {
            Debug.LogError("Spawned monster is missing the Monster script!");
        }
    }

    private Vector3 AdjustSpawnPositionWithinBounds(Vector3 position)
    {
        // Modify this method if you want to ensure the spawn position stays in the playable area
        // For now, it returns the position as-is
        return position;
    }
}
