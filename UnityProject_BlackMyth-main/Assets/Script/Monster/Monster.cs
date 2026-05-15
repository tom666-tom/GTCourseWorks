using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Monster : MonoBehaviour
{
    public Transform player;
    public int maxHealth = 100; 
    private float attackRange = 2f;
    private float chaseRange = 15f;
    private Animator animator;
    private UnityEngine.AI.NavMeshAgent agent;
    private int currentHealth;
    private bool isHit = false;
    private bool isDead = false;
    
    private void Start()
    {
        currentHealth = maxHealth;
	    agent = GetComponent<UnityEngine.AI.NavMeshAgent>();
        animator = GetComponent<Animator>(); 
        animator.SetInteger("State",3);
    }
    
    private void Update()
    {
        if (isDead || isHit) return;

        float distanceToPlayer = Vector3.Distance(transform.position, player.position);
        
        if (distanceToPlayer <= attackRange)
        {
            AttackPlayer();
        }
        else if (distanceToPlayer <= chaseRange)
        {
            ChasePlayer();
        }
        else
        {
            Dance();
        }
    }

    private void FacePlayer()
    {
        Vector3 lookDirection = new Vector3(player.position.x, transform.position.y, player.position.z);
        transform.LookAt(lookDirection);
    }
    
    private void ChasePlayer()
    {
        agent.isStopped = false; 
        animator.SetInteger("State",2);
        agent.destination = player.position;
        FacePlayer();
    }
    
    private void AttackPlayer()
    {
        agent.isStopped = true; 
	    animator.SetInteger("State",1);
        FacePlayer();
    }

    private void Dance()
    {
        animator.SetInteger("State",3);
    }

    public void TakeDamage(int damage)
    {
        if (isDead) return; 
        currentHealth -= 20;
        animator.SetTrigger("Hit");

        if (currentHealth <= 0)
        {
            Die();
        }
    }

    private void Die()
    {
        isDead = true;
        animator.SetBool("Die",true);
        Destroy(gameObject, 3f);
    }

    public void ScaleDifficulty(int level)
    {
        maxHealth += (level - 1) * 20; 
        attackRange += (level - 1) * 0.5f; 
        chaseRange += (level - 1); 
        currentHealth = maxHealth; 
        Debug.Log($"Monster scaled: Level {level}, Health {maxHealth}, AttackRange {attackRange}, ChaseRange {chaseRange}");
    }

}
