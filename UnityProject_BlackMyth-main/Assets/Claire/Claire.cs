using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class Claire : MonoBehaviour {

    private Animator animation_controller;
    private CharacterController character_controller;
    public Vector3 movement_direction;
    public float walking_velocity;
    public Text text;
    public float velocity;
    public float rotationSpeed;
    public int num_lives;
    public bool has_won;
    public bool isJumping;
    public bool isDead;
    public GameObject restartButton;
    public Monster monster;


    public AudioSource loopAudioSource,RunAudioSource;
    public AudioSource AudioSource11;
    public AudioClip singleSoundClip, JumpSoundClip, BoxSoundClip;
    public AudioClip RunSoundClip,loopSoundClip;
    private bool isMoving;
    public string targetTag = "1";
    public Text pickupText;
     GameObject lj;
     
     public int experienceReward = 10;  // exp for collecting trash
     private LevelSystem levelSystem;   // 引用玩家的 LevelSystem 脚本
    void Start()
    {
        loopAudioSource = GetComponent<AudioSource>();
        animation_controller = GetComponent<Animator>();
        character_controller = GetComponent<CharacterController>();
        movement_direction = new Vector3(0.0f, 0.0f, 0.0f);
        walking_velocity = 2.5f;
        velocity = 0.0f;
        rotationSpeed = 50f;
        num_lives = 5;

        has_won = false;
        isJumping = false;
   
        levelSystem = GetComponent<LevelSystem>();
        if (levelSystem == null)
        {
            Debug.LogError("Player is missing LevelSystem component！");
        }
    }


  
    void Update()
    {
        if (Input.GetKey(KeyCode.LeftArrow) || Input.GetKey(KeyCode.A))
        {
            // Rotate the character to the left (negative Y-axis)
            transform.Rotate(0, -rotationSpeed * Time.deltaTime, 0);
        }
        else if (Input.GetKey(KeyCode.RightArrow) || Input.GetKey(KeyCode.D))
        {
            // Rotate the character to the right (positive Y-axis)
            transform.Rotate(0, rotationSpeed * Time.deltaTime, 0);
        }
        if (Input.GetKey(KeyCode.LeftShift) && (Input.GetKey(KeyCode.UpArrow) || Input.GetKey(KeyCode.W)))
        {
            movement_direction = transform.forward;
            velocity = Mathf.Lerp(velocity, walking_velocity * 2.0f, Time.deltaTime);
            animation_controller.SetInteger("state", 5);
         
        }
        else if (Input.GetKey(KeyCode.LeftControl) && (Input.GetKey(KeyCode.UpArrow) || Input.GetKey(KeyCode.W))){
            movement_direction = transform.forward;
            velocity = Mathf.Lerp(velocity, walking_velocity / 2.0f, Time.deltaTime);
            animation_controller.SetInteger("state", 3);
   
        }
        else if (Input.GetKey(KeyCode.LeftControl) && (Input.GetKey(KeyCode.DownArrow) || Input.GetKey(KeyCode.S)))
        {
            movement_direction = - transform.forward;
            velocity = Mathf.Lerp(velocity, walking_velocity / 2.0f, Time.deltaTime);
            animation_controller.SetInteger("state", 4);
         
        }
        else if (Input.GetKey(KeyCode.UpArrow) || Input.GetKey(KeyCode.W))
        {
            movement_direction = transform.forward;
            velocity = Mathf.Lerp(velocity, walking_velocity, Time.deltaTime);
            animation_controller.SetInteger("state", 1);
       
        }
        else if (Input.GetKey(KeyCode.DownArrow) || Input.GetKey(KeyCode.S))
        {
            movement_direction = - transform.forward;
            velocity = Mathf.Lerp(velocity, walking_velocity / 1.5f, Time.deltaTime);
            animation_controller.SetInteger("state", 2);
         
        }
        else
        {
            velocity = 0f;
            animation_controller.SetInteger("state", 0);
        }

            isMoving = character_controller.velocity.magnitude > 0.1f;
        if (isMoving)
        {
            if (Input.GetKey(KeyCode.LeftShift) || Input.GetKey(KeyCode.RightShift))
                {
                    if (!RunAudioSource.isPlaying)
                    {
                    loopAudioSource.Stop();
                    RunAudioSource.Play();
                    }
                }
                else
                {
                    if (!loopAudioSource.isPlaying)
                    {
                    RunAudioSource.Stop();
                    loopAudioSource.Play();
                    }
                }
            }
            else
            {
            loopAudioSource.Stop();
            RunAudioSource.Stop();
            }

        
        // jump CrouchFowards CrouchBackwards Run WalkForwards WalkBackwards Death 
        if (Input.GetKeyDown(KeyCode.Space))
        {
            movement_direction = transform.forward;
            // velocity = Mathf.Lerp(velocity, walking_velocity * 3.0f, Time.deltaTime);
            animation_controller.SetInteger("state", 6);
            JumpSound();
        }

        character_controller.Move(movement_direction * velocity * Time.deltaTime);
        if (Input.GetKeyDown(KeyCode.F) && lj != null)
        {
            Destroy(lj.gameObject);
            animation_controller.SetInteger("state", 8);
            LjSound();
            pickupText.gameObject.SetActive(false);
            if (levelSystem != null)
            {
                levelSystem.AddExperience(experienceReward);
                Debug.Log("Exp 10!");
            }
        }
        if (Input.GetKeyDown(KeyCode.J) && monster != null)
        {
            animation_controller.SetTrigger("attack");
            Vector3 lookDirection = new Vector3(monster.transform.position.x, transform.position.y, monster.transform.position.z);
            transform.LookAt(lookDirection);
            monster.TakeDamage(1);
        }
    }

    public void LjSound()
    {
        AudioSource11.clip = singleSoundClip;
        AudioSource11.Play();
    }

    public void JumpSound()
    {
        AudioSource11.clip = JumpSoundClip;

        AudioSource11.Play();
    }

    private void OnCollisionEnter(Collision collision)
    {
        if (collision.gameObject.tag == "2")
        {
            AudioSource11.clip =BoxSoundClip;
            AudioSource11.Play();
        }
    }

    void OnTriggerEnter(Collider other)
    {
        if (other.CompareTag(targetTag))
        {
            lj = other.gameObject;
            pickupText.gameObject.SetActive(true);
        }
    }

    void OnTriggerExit(Collider other)
    {
        if (other.CompareTag(targetTag))
        {
            lj = null;
            pickupText.gameObject.SetActive(false);
        }
    }
}
